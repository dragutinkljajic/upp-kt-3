import React, { useEffect } from 'react'
import shallow from 'zustand/shallow'
import { Col, Row, Button } from 'react-bootstrap'
import { useParams } from 'react-router-dom'

import history from '../history'
import { useTasksStore } from './state'
import { TaskForm } from './TaskForm'
import { SimpleTask } from './SimpleTask'

export const TaskPage = () => {
  let { taskId } = useParams();
  const { actions, currentTask } = useTasksStore(
    store => ({
      currentTask: store.currentTask,
      actions: store.actions
    }),
    shallow
  )

  useEffect(() => {
    actions.getTask(taskId)
  }, []);

  const handleSubmit = async values => {
    if (values) {
      await actions.submitTaskForm(currentTask.taskId, values)
    }

    history.push('/')
  }

  return (
    <>
      {currentTask && currentTask.formFields && (
        <TaskForm task={currentTask} onSubmit={handleSubmit} />
      )}
      {currentTask && !currentTask.formFields && (
        <SimpleTask task={currentTask} onSubmit={handleSubmit} />
      )}
    </>
  )
}