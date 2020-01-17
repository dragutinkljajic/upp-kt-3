import React, { useEffect } from 'react'
import shallow from 'zustand/shallow'

import { useTasksStore } from '../tasks/state'
import { TaskForm } from '../tasks/TaskForm'
import { SimpleTask } from '../tasks/SimpleTask'

export const RegistrationProcess = () => {
  const { actions, processInstanceId, currentTask, availableTasks } = useTasksStore(
    store => ({
      processInstanceId: store.processInstanceId,
      currentTask: store.currentTask,
      availableTasks: store.availableTasks,
      actions: store.actions
    }),
    shallow
  )

  useEffect(() => {
    actions.startProcess()
    actions.getTasks()
  }, []);

  const handleSubmit = async values => {

    if (values) {
      await actions.submitTaskForm(currentTask.taskId, values)
    }

    await actions.getTasks(processInstanceId)
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