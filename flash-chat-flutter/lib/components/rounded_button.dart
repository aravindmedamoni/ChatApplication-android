import 'package:flutter/material.dart';


class RoundedButton extends StatelessWidget {
  final Color colour;
  final Function onPress;
  final String buttonName;

  RoundedButton(
      {@required this.colour,
        @required this.onPress,
        @required this.buttonName});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.symmetric(vertical: 16.0),
      child: Material(
        elevation: 5.0,
        color: colour,
        borderRadius: BorderRadius.circular(18.0),
        child: MaterialButton(
          onPressed: onPress,
          minWidth: 200.0,
          height: 42.0,
          child: Padding(
            padding: EdgeInsets.symmetric(vertical: 12.0),
            child: Text(
              buttonName,
              style: TextStyle(
                fontSize: 20.0,
                color: Colors.white,
              ),
            ),
          ),
        ),
      ),
    );
  }
}
