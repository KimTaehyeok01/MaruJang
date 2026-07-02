// 로그인 페이지: OAuth2 공급자(Google/Kakao) 로그인 버튼만 제공한다
import { getOAuth2LoginUrl } from '../api/authApi'

export default function LoginPage() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center gap-4">
      <h1 className="text-xl font-medium">마루장 로그인</h1>
      <a
        href={getOAuth2LoginUrl('google')}
        className="rounded bg-blue-600 px-4 py-2 text-white"
      >
        Google로 로그인
      </a>
      <a
        href={getOAuth2LoginUrl('kakao')}
        className="rounded bg-yellow-400 px-4 py-2 text-black"
      >
        Kakao로 로그인
      </a>
    </div>
  )
}
