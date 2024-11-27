// File: src/components/LogDisplay.jsx

import React, { useEffect, useState } from 'react';
import { Card, ListGroup } from 'react-bootstrap';
import SockJS from 'sockjs-client/dist/sockjs'; // Ensure this import is correct
import { Client } from '@stomp/stompjs';

const LogDisplay = () => {
  const [logs, setLogs] = useState([]);

  useEffect(() => {
    const socketUrl = 'http://localhost:8080/ws';
    const stompClient = new Client({
      webSocketFactory: () => new SockJS(socketUrl),
      reconnectDelay: 5000,
      debug: (str) => {
        console.log(str);
      },
    });

    stompClient.onConnect = () => {
      stompClient.subscribe('/topic/logs', (message) => {
        if (message.body) {
          setLogs((prevLogs) => [message.body, ...prevLogs]);
        }
      });
    };

    stompClient.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

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
      {/* Wrap the ListGroup in a div with scrollable style */}
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
