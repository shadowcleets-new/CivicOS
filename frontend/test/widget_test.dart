import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:civicos_app/main.dart';

void main() {
  testWidgets('App starts without error', (WidgetTester tester) async {
    await tester.pumpWidget(const CivicOSApp());
    expect(find.byType(MaterialApp), findsOneWidget);
  });
}
