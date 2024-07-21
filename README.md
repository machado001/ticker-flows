# ticker-flows

Simple Ticker sample, demonstrating advanced Kotlin Flows operations.

We combine two flows and put it in a Data Class.

The ticker is `lifecycle-aware`, because we convert the flows to a `StateFlow`. If user put the UI in background, the count will stop. if screen rotates, the count will continues.

![image](https://github.com/user-attachments/assets/c0d06dd0-e65a-4c5a-88ef-fe8da4a8b56c)


