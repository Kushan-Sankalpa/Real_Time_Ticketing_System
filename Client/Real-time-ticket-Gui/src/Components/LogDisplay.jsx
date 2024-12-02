import React, { useEffect, useState } from 'react';
import { Card, ListGroup } from 'react-bootstrap';
import SockJS from 'sockjs-client/dist/sockjs';
import { Client } from '@stomp/stompjs';

const LogDisplay = () => {
  const [logs, setLogs] = useState([]);

  useEffect(() => {
    const socketUrl = 'http://localhost:8080/ws';
    const stompClient = new Client({
      webSocketFactory: () => new SockJS(socketUrl),
      reconnectDelay: 5000, // Attempt to reconnect every 5 seconds if disconnected
      debug: (str) => {
        console.log(str);
      },
    });


    // Function to handle connection establishment
    stompClient.onConnect = () => {
      // Subscribe to the '/topic/logs' destination
      stompClient.subscribe('/topic/logs', (message) => {
        if (message.body) {
          setLogs((prevLogs) => [message.body, ...prevLogs]);
        }
      });
    };

    // Error handling for STOMP errors
    stompClient.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    // Activate the STOMP client to establish the connection
    stompClient.activate();



    return () => {
      if (stompClient) {
        stompClient.deactivate();
      }
    };
  }, []);

  return (
    <Card>
      <Card.Header>Log Display</Card.Header>
      <div style={{ maxHeight: '300px', overflowY: 'auto' }}>
        <ListGroup variant="flush">
          {logs.map((log, index) => (
            <ListGroup.Item key={index}>{log}</ListGroup.Item>
          ))}
        </ListGroup>
      </div>
    </Card>
  );
};

export default LogDisplay;
