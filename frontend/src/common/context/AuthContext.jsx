// 로그인 상태(accessToken)를 앱 전체에서 공유하기 위한 Context
import { createContext, useContext, useState, useEffect } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [accessToken, setAccessToken] = useState(null)

  useEffect(() => {
    const saved = localStorage.getItem('accessToken')
    if (saved) {
      setAccessToken(saved)
    }
  }, [])

  function login(token) {
    localStorage.setItem('accessToken', token)
    setAccessToken(token)
  }

  function logout() {
    localStorage.removeItem('accessToken')
    setAccessToken(null)
  }

  const value = {
    accessToken,
    isAuthenticated: !!accessToken,
    login,
    logout,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  return useContext(AuthContext)
}
