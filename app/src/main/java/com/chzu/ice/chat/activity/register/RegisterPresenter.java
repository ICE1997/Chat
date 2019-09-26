package com.chzu.ice.chat.activity.register;

public class RegisterPresenter implements IRegisterContract.Presenter {
    private IRegisterContract.View view;
    private RegisterModel model;

    RegisterPresenter(IRegisterContract.View view, RegisterModel model) {
        this.view = view;
        this.model = model;
        view.setPresenter(this);
    }

    @Override
    public void register(String usr,String pwd) {
        model.register(usr, pwd, new RegisterModel.RegisterCallback() {
            @Override
            public void registerSucceed() {
                view.showRegisterSucceed();
            }

            @Override
            public void hasAlreadyRegistered() {
                view.showAlreadyRegistered();
            }
        });
    }
}
