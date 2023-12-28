#include <time.h>
#include <stdio.h>
#include <cstring>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <fstream>
#include <sstream>
#include <iostream>
#include <string>
using namespace std;

#define PORT 5952
#define CLIENT_CAPACITY 20
#define DataSize 2000000
// #define SERVER_IP "127.0.0.1" // 指定服务端的IP，记得修改为你的服务端所在的ip
#define SERVER_IP "10.181.252.40"
struct Client_State
{
    int socket;
    char address[16];
    int port;
    int alive;
};

struct Clients_List
{
    int clients_number;
    struct Client_State client_state_list[CLIENT_CAPACITY];
};

struct Message
{
    char data[DataSize];
};

class myHttpRequest
{
private:
    string method; // method+URL+协议
    string url;
    string protocol;
    int socket;

public:
    myHttpRequest(string method, string url, string protocol, int socket)
    {
        this->method = method;
        this->url = "." + url;
        this->protocol = protocol;
        this->socket = socket;
    };
    int response(istringstream &inflow);
};

struct Clients_List global_clients;
pthread_t server_threads[CLIENT_CAPACITY];

void *connection_handler(void *socket);
void *server_handler(void *arg);
void remove_r(char *bytes, int length);
string get_content_type(string path);

int main()
{
    int exit_signal = 1;
    pthread_t server_thread;
    pthread_create(&server_thread, NULL, server_handler, &exit_signal);

    memset(&global_clients, 0, sizeof(global_clients));            // 创建套接字，it just creates interface
    int server_socket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP); // 将套接字和IP、端口绑定
    struct sockaddr_in server_address;                             // IPv4地址结构体
    memset(&server_address, 0, sizeof(server_address));            // 每个字节都用0填充
    server_address.sin_family = AF_INET;                           // 使用IPv4地址
    server_address.sin_addr.s_addr = inet_addr("127.0.0.1");       // 服务器IP地址(这里使用的是本机地址，INADDR_ANY)  htonl(INADDR_ANY);
    server_address.sin_port = htons(PORT);                         // 端口

    bind(server_socket, (struct sockaddr *)&server_address, sizeof(server_address)); // 这里地址强制转换, 继承又不完全继承
    listen(server_socket, CLIENT_CAPACITY);                                          // 20表示服务器端队列长度，可以有20个clients等待接收客户端请求
    while (exit_signal)
    {
        struct sockaddr_in client_address;
        socklen_t client_address_size = sizeof(client_address);
        int client_socket = accept(server_socket, (struct sockaddr *)&client_address, &client_address_size);
        global_clients.clients_number++;
        pthread_t connection_thread;
        pthread_create(&connection_thread, NULL, connection_handler, &client_socket);
        
    }
    printf("All the clients service threads has been shut down successfully!\n");
    printf("Good bye!\n");
    pthread_cancel(server_thread);
    close(server_socket);

    return 0;
}
/**
 * 用于处理http请求
 */
void *connection_handler(void *socket)
{
    int client_socket = *(int *)socket;
    struct Message client_message;
    int i;
    int offset;
    time_t timer;

    memset(client_message.data, 0, DataSize);
    int flag = recv(client_socket, &client_message, sizeof(client_message), 0);

    remove_r(client_message.data, sizeof(client_message));
    cout << client_message.data << endl;
    string method, url, protocol, empty;

    istringstream in_data(client_message.data);
    in_data >> method >> url >> protocol;
    getline(in_data, empty, '\n');
    myHttpRequest my_request(method, url, protocol, client_socket);
    my_request.response(in_data);

    return NULL;
}

void remove_r(char *bytes, int length)
{
    int i;
    for (i = 0; i < length; i++)
    {
        if (bytes[i] == '\r')
        {
            bytes[i] = ' ';
        }
    }
}

string get_content_type(string path)
{
    if (path.find(".jpg") != -1)
    {
        return "content-type:image/jpg\r\n";
    }
    else if (path.find(".html") != -1)
    {
        return "content-type:text/html;charset=UTF-8\r\n";
    }
    else
    {
        return "content-type:text/plain;charset=UTF-8\r\n";
    }
}

int myHttpRequest::response(istringstream &inflow)
{
    if (this->method == "GET")
    {
        FILE *resource;
        resource = fopen(this->url.data(), "r");
        try
        {
            if (resource == NULL || this->url == "./")
            {
                string header = "HTTP/1.1 404 file not found \r\nContent-Type:text/html;charset=UTF-8\r\nContent-Length:22 \r\n\r\n";
                string content = "<h1>404 Not Found</h1>";
                string response;
                response = header + content;
                send(this->socket, response.data(), response.length(), 0);
            }
            else
            {
                string header = "HTTP/1.1 200 OK\r\n";
                header = header + get_content_type(this->url);
                char *binary_data = new char[DataSize];
                int size = fread(binary_data, 1, DataSize, resource);
                header = header + "Content-Length:" + to_string(size) + "\r\n\r\n";
                send(this->socket, header.data(), header.length(), 0);
                send(this->socket, binary_data, size, 0);
                delete[] binary_data;
                fclose(resource);
            }
        }
        catch (...)
        {
            string error = "HTTP/1.1 404 file not found \r\nContent-Type:text/html;charset=UTF-8\r\nContent-Length:22 \r\n\r\n<h1>404 Not Found</h1>";
            send(this->socket, error.data(), error.length(), 0);
        }
    }
    else if (this->method == "POST" && this->url.find("dopost") != -1)
    {
        string temp;
        try
        {
            getline(inflow, temp, '\n');
            while (temp != " ")
            {
                getline(inflow, temp, '\n');
                if (temp.find("Content-Length") != -1)
                {
                    string str_length = temp.substr(temp.find("Content-Length") + 16);
                    int length;
                    stringstream(str_length) >> length;
                }
            }
            string parameter;
            getline(inflow, parameter, '\n');
            int split_index = parameter.find('&');
            string login_part = parameter.substr(0, split_index);
            string login = login_part.substr(login_part.find('=') + 1);
            string pass_part = parameter.substr(split_index + 1);
            string pass = pass_part.substr(pass_part.find('=') + 1);
            string message;
            if (login == "3210105952" && pass == "5952")
            {
                message = "<html><head><meta charset=\"UTF-8\"></head><body>登陆成功</body></html>";
            }
            else
            {
                message = "<html><head><meta charset=\"UTF-8\"></head><body>登陆失败</body></html>";
            }
            string header = "HTTP/1.1 200 OK \r\nContent-Type:text/html \r\n";
            header = header + "Content-Length:" + to_string(message.length()) + "\r\n\r\n";
            send(this->socket, header.data(), header.length(), 0);
            send(this->socket, message.data(), message.length(), 0);
        }
        catch (...)
        {
            string error = "HTTP/1.1 404 file not found \r\nContent-Type:text/html;charset=UTF-8\r\nContent-Length:22 \r\n\r\n<h1>404 Not Found</h1>";
            send(this->socket, error.data(), error.length(), 0);
        }
    }
    return 1;
}

/**
 * 用于响应退出请求exit,关闭其他线程和socket
 */
void *server_handler(void *arg)
{
    printf("Server is running now!\n");
    printf("You can shutdown the server at any time, just input 'exit'\n\n");
    char instruction[20];
    int i;
    while (1)
    {
        sleep(2);
        scanf("%s", instruction);
        if (strcmp(instruction, "exit") == 0)
        {
            printf("Server is shutting down, client thread will be closed!\n");
            for (i = 0; i < CLIENT_CAPACITY; i++)
            {
                if (server_threads[i] != 0)
                {
                    pthread_cancel(server_threads[i]);
                    server_threads[i] = 0;
                }
            }
            exit(0);
        }
        else
        {
            memset(instruction, 0, 20);
            printf("Unrecognized instruction, if you want to shut down, input 'exit'\n");
        }
    }
    return NULL;
}
