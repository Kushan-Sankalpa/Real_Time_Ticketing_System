// File: src/components/TicketStatistics.jsx

import React from 'react';
import { Table } from 'react-bootstrap';

const TicketStatistics = ({ statistics }) => {
  return (
    <div>
      <h5>Ticket Statistics</h5>
      <Table striped bordered hover>
        <tbody>
          <tr>
            <th>Total Tickets Sold</th>
            <td>{statistics.totalTicketsSold}</td>
          </tr>
          <tr>
            <th>Total Tickets Released</th>
            <td>{statistics.totalTicketsReleased}</td>
          </tr>
          <tr>
            <th>Tickets Yet to Release</th>
            <td>{statistics.ticketsYetToRelease}</td>
          </tr>
        </tbody>
      </Table>
    </div>
  );
};

export default TicketStatistics;
