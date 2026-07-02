// OAuth2 로그인 성공 후 백엔드가 accessToken을 쿼리 파라미터로 붙여 리다이렉트하는 페이지
import { useEffect } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '../../../common/context/AuthContext'

export default function OAuth2RedirectPage() {
  const [searchParams] = useSearchParams()
  const { login } = useAuth()
  const navigate = useNavigate()

  useEffect(() => {
    const token = searchParams.get('token')
    if (token) {
      login(token)
    }
    navigate('/')
  }, [searchParams, login, navigate])

  return <p className="p-4">로그인 처리 중...</p>
}
