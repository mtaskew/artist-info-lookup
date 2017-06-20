package com.marquarius.response;

/**
 * Created by marquariusaskew on 5/24/17.
 */
public class Response {
    private DialogAction dialogAction;

    public DialogAction getDialogAction() {
        return dialogAction;
    }

    public void setDialogAction(DialogAction dialogAction) {
        this.dialogAction = dialogAction;
    }

    public static Response generateInfoUrlResponse(String responseMessage) {
        Response response = new Response();
        Message message = new Message();
        message.setContent(responseMessage);
        message.setContentType("PlainText");
        DialogAction dialogAction = new DialogAction();
        dialogAction.setMessage(message);
        dialogAction.setFulfillmentState("Fulfilled");
        dialogAction.setType("Close");
        response.setDialogAction(dialogAction);

        return response;
    }
}
