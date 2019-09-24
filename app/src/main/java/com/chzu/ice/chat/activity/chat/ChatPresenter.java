package com.chzu.ice.chat.activity.chat;

public class ChatPresenter implements IChatContract.Presenter {
    private ChatModel chatModel;
    private IChatContract.View chatView;

    ChatPresenter(IChatContract.View chatView, ChatModel chatModel) {
        this.chatView = chatView;
        this.chatModel = chatModel;
        this.chatView.setPresenter(this);
    }

    @Override
    public void publish(String s) {
        chatModel.publish(s, new ChatModel.PublishCallback() {
            @Override
            public void publishSucceed() {
                chatView.showPublishSucceed();
            }

            @Override
            public void publishFailed() {
                chatView.showPublishFailed();
            }
        });
    }
}
