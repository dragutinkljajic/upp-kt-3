import React, { useEffect } from 'react'
import shallow from 'zustand/shallow'
import { Col, Row, Button } from 'react-bootstrap'

import history from '../history'
import { useTasksStore } from './state'

export const AssignedTasksList = () => {

  const { actions, assignedTasks } = useTasksStore(
    store => ({
      assignedTasks: store.assignedTasks,
      actions: store.actions
    }),
    shallow
  )

  useEffect(() => {
    const user = localStorage.getItem("user")

    if (!user) {
      history.push('/signin')
    }

    actions.getAssignedTasks()
  }, []);

  console.log(assignedTasks)

  return (
    <>
      <Row>
        <Col md={3}>
          Assigned Tasks
        </Col>
      </Row>
      {assignedTasks && assignedTasks.length > 0 && assignedTasks.map(task => (
        <Row key={task.taskId}>
          <Col md={3}>
            <a href={`/task/${task.taskId}`}>{task.name}</a>
          </Col>
        </Row>
      ))}
    </>
  )
}