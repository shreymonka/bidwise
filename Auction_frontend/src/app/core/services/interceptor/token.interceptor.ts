// token-interceptor.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { LoginServiceService } from '../login-service/login-service.service';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  // Use Angular's inject function to get the LoginServiceService instance
  const loginService = inject(LoginServiceService);

  // Retrieve the token from the LoginServiceService
  const token = loginService.getToken();

  // Check if the token is available
  if (token) {
    // Clone the request and add the Authorization header with the token
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    // Pass the cloned request to the next handler in the chain
    return next(clonedRequest);
  }

  // If no token, pass the original request to the next handler
  return next(req);
};
