import create from 'zustand'
import axios from 'axios'

import { apiUrl } from '../helpers/config'

const api = {
  auth: {
    login: (values) => axios.post(apiUrl + 'auth/login', values),
    logout: () => axios.get(apiUrl + 'auth/logout')
  }
}

export const [useAuthStore] = create((set, get) => ({
  user: localStorage.getItem('user'),

  actions: {
    async login(values) {
      const user = get().user

      if (!user) {
        const { data } = await api.auth.login(values)
        console.log(data)
        localStorage.setItem('user', JSON.stringify(data))
        set({
          user: data
        })
      }
    },
    async logout() {
      await api.auth.logout()

      localStorage.removeItem('user')
      set({
        user: null
      })
    }
  }
}))