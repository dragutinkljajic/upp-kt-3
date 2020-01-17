import React from 'react'
import ReactDOM from 'react-dom'
import 'bootstrap/dist/css/bootstrap.min.css'
import axios from 'axios'

import './index.css'
import App from './App'

axios.defaults.withCredentials = true

ReactDOM.render(<App />, document.getElementById('root'))
