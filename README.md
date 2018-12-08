# Quizzical App for Android v2

## 1. Introduction

This repository contains the source code of the Quizzical Android Application.
The source code for the backend API can be found [here](https://github.com/w-k-s/aws-lambda-quizzical).

## 2. History

The Quizzical app and API were originally developed in 2012 as part of a university coursework.
I've continued to work on these projects in order to try out new tools on Android and on the backend.

The advantage of using this project as a testbed is that it only contains three screens (`categories`, `quiz` and `score`) so changes can be made easily, usually in one day.

- **[Version 1](https://github.com/w-k-s/Android-Quizzical-v1) (2013)** Amateur Android project in Java (no design patterns, no database, `AsyncTasks` and `ListView`s).
- **Version 2 (2015)** (this repository): Rewritten in Kotlin with coroutines, MVP, Dagger, Room and Retrofit.
