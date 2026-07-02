// 모든 API 요청에 사용하는 axios 인스턴스. 저장된 토큰을 자동으로 헤더에 붙여준다
import axios from 'axios'

const axiosInstance = axios.create({
  baseURL: '/api',
})

axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default axiosInstance
