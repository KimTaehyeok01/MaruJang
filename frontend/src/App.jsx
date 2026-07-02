// 앱 최상위 컴포넌트: 라우터와 인증 Context를 감싼다
import { BrowserRouter } from 'react-router-dom'
import { AuthProvider } from './common/context/AuthContext'
import AppRouter from './routes/AppRouter'

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppRouter />
      </AuthProvider>
    </BrowserRouter>
  )
}
