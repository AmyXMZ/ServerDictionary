public serverUI(HashMap<Integer, Clients> clientsHashMap, ServerSocket serverSocket){
    this.serverFrame = new JFrame();
    this.area = new JTextArea();
    this.client =client;
    this.buttonMap = new HashMap<Integer, JButton>();
    this.serverSocket = serverSocket;
}