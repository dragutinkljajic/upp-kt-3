import create from 'zustand'
import axios from 'axios'

import { apiUrl } from '../helpers/config'

const api = {
  processes: {
    startProcess: () => axios.get(apiUrl + 'welcome/get')
  },
  tasks: {
    getTasks: (processInstanceId) => axios.get(apiUrl + 'welcome/get/tasks/' + processInstanceId),
    getTask: (taskId) => axios.get(apiUrl + 'welcome/tasks/' + taskId),
    getAssignedTasks: () => axios.get(apiUrl + 'welcome/get/assigned'),
    claimTask: (taskId) => axios.post(apiUrl + 'welcome/tasks/claim/' + taskId, {}),
    submitTaskForm: (taskId, values) => axios.post(apiUrl + 'welcome/post/' + taskId, values),
    completeTask: (taskId) => axios.post(apiUrl + 'welcome/tasks/complete/' + taskId, {})
  }
}

export const [useTasksStore] = create((set, get) => ({
  processInstanceId: 0,
  currentTask: null,
  availableTasks: null,
  assignedTasks: null,

  actions: {
    async startProcess() {
      const { data } = await api.processes.startProcess()
      
      set(() => ({
        processInstanceId: data.processInstanceId,
        currentTask: data
      }))

    },
    async getTasks() {
      const { data } = await api.tasks.getTasks(get().processInstanceId)
      console.log(data)
      set(() => ({
        currentTask: data[0],
        availableTasks: data
      }))
    },
    async claimTask(taskId) {
      const { data } = await api.tasks.claimTask(taskId)
      console.log(data)
    },
    async submitTaskForm(taskId, values) {
      await api.tasks.submitTaskForm(taskId, values)
    },
    async completeTask(taskId) {
      await api.tasks.completeTask(taskId)
    },
    async getAssignedTasks() {
      const { data } = await api.tasks.getAssignedTasks()
      set(() => ({
        assignedTasks: data
      }))
    },
    async getTask(taskId) {
      const { data } = await api.tasks.getTask(taskId)

      set(() => ({
        currentTask: data
      }))
    }
  }
}))
