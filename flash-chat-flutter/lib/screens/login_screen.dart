import 'package:flutter/material.dart';
import 'package:flash_chat/constants.dart';
import 'package:flash_chat/components/rounded_button.dart';

class LoginScreen extends StatefulWidget {
  static const String id = '/loginScreen';

  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Padding(
          padding: EdgeInsets.symmetric(horizontal: 24.0),
          child: SingleChildScrollView(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: <Widget>[
                Hero(
                  tag: 'logo',
                  child: Container(
                    height: 200.0,
                    child: Image.asset('images/logo.png'),
                  ),
                ),
                SizedBox(
                  height: 48.0,
                ),
                TextField(
                  style: TextStyle(
                    color: Colors.black87,
                  ),
                  onChanged: (value) {
                    //Do something with the user input.
                  },
                  decoration: kTextFieldDecoration.copyWith(
                    labelText: 'Enter your Email',
                    prefixIcon: Icon(
                      Icons.mail,
                      color: Colors.grey,
                    ),
                  ),
                ),
                SizedBox(
                  height: 8.0,
                ),
                TextField(
                  style: TextStyle(
                    color: Colors.black87,
                  ),
                  onChanged: (value) {
                    //Do something with the user input.
                  },
                  decoration: kTextFieldDecoration.copyWith(
                    labelText: 'Enter your password',
                    prefixIcon: Icon(
                      Icons.lock,
                      color: Colors.grey,
                    ),
                  ),
                ),
                SizedBox(
                  height: 24.0,
                ),
                Padding(
                    padding: EdgeInsets.symmetric(vertical: 30.0),
                    child: RoundedButton(
                        colour: Colors.lightBlueAccent,
                        onPress: null,
                        buttonName: 'Log In')),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
