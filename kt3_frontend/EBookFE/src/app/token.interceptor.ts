import { Injectable } from "@angular/core";
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from "@angular/common/http";
import { Observable } from "rxjs";
import { AuthService } from "./services/auth/auth-service.service";


@Injectable()
export class TokenInterceptor implements HttpInterceptor {
    constructor(private authService: AuthService) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // add authorization header with jwt token if available
        var token = this.authService.getToken();
        if (token) {
            request = request.clone({
                setHeaders: { 
                    Authorization: `Bearer ${token}`
                }
            });
        }

        return next.handle(request);
    }
}