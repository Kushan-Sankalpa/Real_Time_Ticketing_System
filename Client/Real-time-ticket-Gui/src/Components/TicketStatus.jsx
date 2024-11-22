// File: src/components/TicketStatus.jsx

import React, { useState, useEffect } from "react";
import apiClient from "../axios";
import { Card } from 'react-bootstrap';

const TicketStatus = () => {
  const [availableTickets, setAvailableTickets] = useState(0);

  const fetchTicketCount = async () => {
    try {
      const response = await apiClient.get("/tickets/getAvailableTickets");
      setAvailableTickets(response.data);
    } catch (error) {
      console.error("Error fetching ticket count:", error);
    }
  };

  useEffect(() => {
    fetchTicketCount(); // Initial fetch
    const interval = setInterval(fetchTicketCount, 3000); // Poll every 3 seconds
    return () => clearInterval(interval); // Cleanup on unmount
  }, []);

  return (
    <Card className="mt-4">
      <Card.Header>Real-Time Ticket Status</Card.Header>
      <Card.Body>
        <h5>Available Tickets: {availableTickets}</h5>
      </Card.Body>
    </Card>
  );
};

export default TicketStatus;