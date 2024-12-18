import React, { useState, useEffect } from 'react';
import apiClient from '../axios';
import { Card } from 'react-bootstrap';

const TicketStatus = () => {
  const [availableTickets, setAvailableTickets] = useState(0);

  // Function to fetch the current available ticket count
  const fetchTicketCount = async () => {
    try {
      const response = await apiClient.get('/tickets/getAvailableTickets');
      setAvailableTickets(response.data);
    } catch (error) {
      console.error('Error fetching ticket count:', error);
    }
  };

  useEffect(() => {
    fetchTicketCount();

    // Poll the ticket count every second
    const interval = setInterval(fetchTicketCount, 1000);
    return () => clearInterval(interval);
  }, []);

  return (
    <Card>
      <Card.Header>Real-Time Ticket Status</Card.Header>
      <Card.Body>
        <h5>Available Tickets: {availableTickets}</h5>
      </Card.Body>
    </Card>
  );
};

export default TicketStatus;
