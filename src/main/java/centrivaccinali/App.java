package centrivaccinali;

import centrivaccinali.controllers.LoginController;
import centrivaccinali.controllers.RegisterController;
import centrivaccinali.web.ServerJSONHandler;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The App class, tasked with initializing our window and application.
 *
 *       @author SEDE COMO
 *       @author Samuele Barella - mat.740688
 *       @author Lorenzo Pengue - mat.740727
 *       @author Andrea Pini - mat.740675
 *
 */
public class App extends Application {

    private static Scene loginScene;
    private static Scene registerScene;
    private static Scene homeScene;
    private ServerJSONHandler s;

    @Override
    public void start(Stage stage) throws IOException {

        s = new ServerJSONHandler();
        FXMLLoader loginLoader = loadFXML("login");
        FXMLLoader registerLoader = loadFXML("register");

        loginScene = new Scene(loginLoader.load(), 570, 400);
        registerScene = new Scene(registerLoader.load(), 570, 400);

        stage.setScene(loginScene);

        LoginController loginController = loginLoader.getController();
        RegisterController registerController = registerLoader.getController();

        loginController.loginButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String checkUser = loginController.usernameText.getText().toString();
                String checkPassword = loginController.passwordText.getText().toString();

                if(loginController.login(s, checkUser, checkPassword)){
                    loginController.errorLabel.setText("Success");
                    loginController.errorLabel.setTextFill(Color.GREEN);

                    try {
                        FXMLLoader homeLoader = loadFXML("home");
                        homeScene = new Scene(homeLoader.load(), 800, 600);
                        stage.setScene(homeScene);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    loginController.errorLabel.setText("Incorrect user or password");
                    loginController.errorLabel.setTextFill(Color.RED);
                }
                loginController.usernameText.setText("");
                loginController.passwordText.setText("");
            }
        });

        loginController.registerButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                stage.setScene(registerScene);
            }
        });

        registerController.registrationButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                boolean res;
                if(res = registerController.register(s)) {
                    System.out.println(res);
                    stage.setScene(loginScene);
                }else registerController.error.setText("Error");
            }
        });

        registerController.backButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                stage.setScene(loginScene);
            }
        });

        stage.show();
    }


    /**
     * Sets root.
     *
     * @param fxml the fxml
     * @throws IOException the io exception
     */
    static void setRoot(String fxml) throws IOException {
        loginScene.setRoot(loadFXML(fxml).load());
    }

    private static FXMLLoader loadFXML(String fxml) throws IOException {
        return new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch();
    }

}