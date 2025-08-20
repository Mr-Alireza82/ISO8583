# ISO8583 Kotlin Packer/Unpacker

A lightweight [ISO8583](https://en.wikipedia.org/wiki/ISO_8583) message packer and unpacker implemented in Kotlin using the Builder Pattern.

## ✨ Features

- ✅ Message building and parsing
- ✅ Customizable field templates
- ✅ Builder pattern for fluent APIs
- ✅ Supporting [MTLS](https://en.wikipedia.org/wiki/Mutual_authentication)
- ✅ Supporting Asynchronous Non-Blocking IO

<br>

## 🏗️ Architecture Overview

This library is designed with a **clean separation of responsibilities**, making it modular and easy to extend.  
It consists of three core layers:

### 1️⃣ Client Logic
🔌 Handles TCP connections, retries, and response reading.  
The client is only responsible for **transporting the message** across the network.  

### 2️⃣ Message Logic
🧱 Uses the **Builder Pattern** to fluently construct ISO8583 messages.  
The message structure is generic, but its **values and fields** are determined by the protocol.  

### 3️⃣ Protocol Logic
📜 Defines the **rules and templates** for the ISO8583 protocol version in use.  
Each protocol provides its own builder configuration.  
- Same builder structure  
- Different field definitions and values  

---

✅ **This separation allows you to:**  
- Reuse the **Client** for networking without touching message construction  
- Swap out **Protocol Logic** while keeping the same **Message** interface  
- Maintain **clean, extensible, and testable** code  

<br>

## 🚀 Getting Started

### Installation
Library is available at Maven Centeral so you can add this dependency to your build.gradle.kts (Module: app) like below:
```kotlin
// Gradle
implementation("com.iso:iso8583:1.0.0")
```

## ⚡ Usage Example
Here’s how to start a TCP connection with timeout and perform read/write operations using the Builder Pattern:
```kotlin
override fun startTcpSession() {
    val client = Client.Companion.create()
        .config("78.157.33.208", 4142)
        .buffer("your-ISO8583-message-here".toByteArray())
        .timeout(3000L, TimeUnit.MILLISECONDS)
        .retryCount(3)
        .enableSSL(false)
        .hasResponseLength(true, 4)
        .enableChunkResponse(true)
        .build()

    viewModelScope.launch {
        client.connect()
            .distinctUntilChangedBy { it::class }
            .collect { state ->

                if (_connectionState.value?.javaClass != state.javaClass) {
                    _connectionState.value = state
                }

                if (state is ConnectionState.ConnectSuccess) {
                    writeToTcp(client)
                } else if (state is ConnectionState.ConnectFailed) {
                    client.disconnect()
                }
        }
    }
}

override fun writeToTcp(client: TCP) {
    viewModelScope.launch {
        client.write().collect { state ->
            _writeState.value = state
        }
    }
}

override fun readFromTcp(client: TCP) {
    viewModelScope.launch {
        client.read().collect { state ->
            _readState.value = state

            if (state is ReadState.ReadSuccess) {
                // TODO: parse ISO8583 response here
                // state.response
            } else if (state is ReadState.ReadFailed) {
                client.disconnect()
            }
        }
    }
}
```
