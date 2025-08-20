class Main : TCPViewModelImpl() {

    private val _connectionState = MutableStateFlow<ConnectionState?>(null)
    val connectionState: StateFlow<ConnectionState?> = _connectionState

    private val _writeState = MutableStateFlow<WriteState?>(null)
    val writeState: StateFlow<WriteState?> = _writeState

    private val _readState = MutableStateFlow<ReadState?>(null)
    val readState: StateFlow<ReadState?> = _readState

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
                    } else if(state is ConnectionState.ConnectFailed) {
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
                    // TODO: use it and give it to parser
                    //state.response
                } else if(state is ReadState.ReadFailed) {
                    client.disconnect()
                }
            }
        }
    }
}
