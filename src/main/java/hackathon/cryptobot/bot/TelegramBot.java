package hackathon.cryptobot.bot;

import hackathon.cryptobot.bot.config.BotConfig;
import hackathon.cryptobot.core.models.Session;
import hackathon.cryptobot.core.models.User;
import hackathon.cryptobot.core.repository.SessionRepository;
import hackathon.cryptobot.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserService service;

    @Autowired
    private SessionRepository redis;


    public TelegramBot(BotConfig config) {
        this.config = config;

        List<BotCommand> commands = List.of(
                new BotCommand(Commands.START, Response.START),
                new BotCommand(Commands.REGISTRATION, Response.REGISTRATION),
                new BotCommand(Commands.ACCOUNT, Response.ACCOUNT),
                new BotCommand(Commands.INFO, Response.INFO));


        try {
            this.execute(new SetMyCommands(commands,
                    new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
             e.getMessage();
        }
    }

    final BotConfig config;

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case Commands.START:
                    sendMessage(update.getMessage().getChatId(), "Вы начали работу с ботом");
                    break;
                case Commands.REGISTRATION:
                    register(update.getMessage(), chatId);
                    break;
                case Commands.ACCOUNT:
                   accountInfo(update.getMessage().getChat().getUserName(), update.getMessage());
                    break;
                case Commands.SESSION:
                    newSession(update.getMessage(), chatId);
                    break;
                case Commands.INFO:
                    sendMessage(update.getMessage().getChatId(), "Бот предназначен для отслеживания курса интересующих вас криптовалют");
                    break;
                default:
            }
        }

         else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

          if (callbackData.equals(Buttons.OVER_SESSION)) {
              executeEditMessageText( "Вы закончили сессию", chatId, messageId);
          }

            if (callbackData.equals(Buttons.YES_BUTTON)) {
                if (!isExist(update.getCallbackQuery().getMessage())) {
                    executeEditMessageText("Вы согласились пройти регистрацию", chatId, messageId);
                    agree(update.getCallbackQuery().getMessage());
                }
            }
         else if (callbackData.equals(Buttons.NO_BUTTON)) {
                executeEditMessageText( "Вы отказались проходить регистрацию", chatId, messageId);
            }
        }
    }

    public void newSession (Message message, long chatId) {


        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(String.valueOf(chatId));
        messageToSend.setText("Процесс сессии запущен. Актуальная информация будет приходить вам каждые 5 секунд" + "\n" + "\n"
                + "Что бы завершить сессию нажмите на кнопку ниже");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var yesButton = new InlineKeyboardButton();
        yesButton.setText("Закончить сессию");
        yesButton.setCallbackData(Buttons.OVER_SESSION);

        rowInLine.add(yesButton);
        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        messageToSend.setReplyMarkup(markupInLine);


        Session session = new Session();


        session.setId(1L);
        session.setStartedAt(LocalDateTime.now());
        session.setOverAt(LocalDateTime.now());
        session.setUsername(message.getChat().getUserName());
        redis.save(session);

        System.out.println(redis.findAll());
        executeMessage(messageToSend);
    }

    public void accountInfo(String username, Message message) {
        Optional<User> optional = service.findByUsername(username);

        User user =
               new User(optional.get().getUsername(),
                       optional.get().getFirstName(),
               optional.get().getLastName(),
               optional.get().getInfo(),
               optional.get().getTime());

        if (user.getFirstName().equals(null)) {
            sendMessage(message.getChatId(), "Имя: " + user.getLastName() + "\n" +
                    "Telegram ID: " + user.getUsername() + "\n" + "" +
                    "Дата регистрации: " + user.getTime());
        } else if (!user.getFirstName().equals(null)) {
            sendMessage(message.getChatId(), "Имя: " + user.getFirstName() + "\n" +
                    "Telegram ID: " + user.getUsername() + "\n" + "" +
                    "Дата регистрации: " + user.getTime());
        }
    }
    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }

    private void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.getMessage();
        }
    }

    public boolean isExist (Message message) {
        boolean bool = false;
        if (service.findByUsername(message.getChat().getUserName()).isPresent()) {
            executeEditMessageText("Данный пользователь уже есть в базе", message.getChatId(), message.getMessageId());
        bool = true;
        }
        return bool;
    }

    private void executeEditMessageText(String text, long chatId, long messageId){
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.getMessage();
        }
    }


    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    private void register(Message message, long chatId) {
        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(String.valueOf(chatId));
        messageToSend.setText("Вы действительно хотите пройти регистрацию?" + "\n" + "\n"
                    + "Проходя регистрация вы соглашаетесь c пользовательским соглашением, все ваши данные будут отправлены нам");

            InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();

            var yesButton = new InlineKeyboardButton();
            yesButton.setText("Да, согласен");
            yesButton.setCallbackData(Buttons.YES_BUTTON);

            var noButton = new InlineKeyboardButton();
            noButton.setText("Нет");
            noButton.setCallbackData(Buttons.NO_BUTTON);


            rowInLine.add(yesButton);

            rowInLine.add(noButton);
            rowsInLine.add(rowInLine);

            markupInLine.setKeyboard(rowsInLine);
            messageToSend.setReplyMarkup(markupInLine);

            executeMessage(messageToSend);
    }

    public void waitAnyTime () throws InterruptedException {
        wait(50000);
    }


        public void agree (Message message) {
            User user = new User(
                    message.getChat().getUserName(),
                    message.getChat().getFirstName(),
                    message.getChat().getLastName(),
                    message.getChat().getDescription(),
                    LocalDateTime.now()
            );
            sendMessage(message.getChatId(), "Вы успешно прошли регистрацию");
            service.save(user);
        }

    }


