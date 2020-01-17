import React, { useState } from 'react'
import { Col, Row, Button } from 'react-bootstrap'

export const SimpleTask = ({ task, onSubmit }) => {
  
  const handleClick = async () => {
    onSubmit()
  }

  return (
    <>
      <Row>
        <Col md={3}>
          {task.name}
        </Col>
      </Row>
      <Row>
        <Col md={3}>
          <Button onClick={handleClick} />
        </Col>
      </Row>
    </>
  )
}