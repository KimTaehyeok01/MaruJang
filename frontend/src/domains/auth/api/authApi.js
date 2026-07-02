// 인증 관련 API. OAuth2 로그인은 리다이렉트 방식이라 별도 axios 호출이 아니라 URL 이동으로 시작한다
const BACKEND_BASE_URL = 'http://localhost:8080'

export function getOAuth2LoginUrl(provider) {
  return `${BACKEND_BASE_URL}/oauth2/authorization/${provider}`
}
