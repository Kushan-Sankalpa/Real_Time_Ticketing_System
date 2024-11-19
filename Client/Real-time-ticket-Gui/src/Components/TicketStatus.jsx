// File: src/components/TicketStatus.jsx

import React, { useState, useEffect } from "react";
import apiClient from "../axios";

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
    fetchTicketCount();
    const interval = setInterval(fetchTicketCount, 3000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div>
      <h2>Real-Time Ticket Status</h2>
      <p>Available Tickets: {availableTickets}</p>
    </div>
  );
};

export default TicketStatus;
