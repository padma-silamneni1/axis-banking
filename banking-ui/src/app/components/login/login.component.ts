import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, LoginRequest, RegisterRequest } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <h1>Axis Banking</h1>
          <p>Secure Banking Platform</p>
        </div>

        <div class="tab-switch">
          <button [class.active]="activeTab === 'login'" (click)="activeTab = 'login'">Login</button>
          <button [class.active]="activeTab === 'register'" (click)="activeTab = 'register'">Register</button>
        </div>

        @if (activeTab === 'login') {
          <form (ngSubmit)="login()">
            <div class="form-group">
              <label>Username</label>
              <input type="text" [(ngModel)]="loginData.username" name="username" required placeholder="Enter username">
            </div>
            <div class="form-group">
              <label>Password</label>
              <input type="password" [(ngModel)]="loginData.password" name="password" required placeholder="Enter password">
            </div>
            <button type="submit" class="btn-primary" [disabled]="loading">
              {{ loading ? 'Signing in...' : 'Sign In' }}
            </button>
          </form>
        }

        @if (activeTab === 'register') {
          <form (ngSubmit)="register()">
            <div class="form-group">
              <label>Full Name</label>
              <input type="text" [(ngModel)]="registerData.fullName" name="fullName" required>
            </div>
            <div class="form-group">
              <label>Email</label>
              <input type="email" [(ngModel)]="registerData.email" name="email" required>
            </div>
            <div class="form-group">
              <label>Username</label>
              <input type="text" [(ngModel)]="registerData.username" name="username" required>
            </div>
            <div class="form-group">
              <label>Password</label>
              <input type="password" [(ngModel)]="registerData.password" name="password" required minlength="6">
            </div>
            <button type="submit" class="btn-primary" [disabled]="loading">
              {{ loading ? 'Creating account...' : 'Create Account' }}
            </button>
          </form>
        }

        @if (error) {
          <div class="error-message">{{ error }}</div>
        }
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      display: flex; justify-content: center; align-items: center;
      min-height: 100vh; background: linear-gradient(135deg, #97144d 0%, #4a0a27 100%);
    }
    .login-card {
      background: white; border-radius: 12px; padding: 40px;
      width: 400px; box-shadow: 0 20px 60px rgba(0,0,0,0.3);
    }
    .login-header { text-align: center; margin-bottom: 30px; }
    .login-header h1 { color: #97144d; margin: 0; font-size: 28px; }
    .login-header p { color: #666; margin: 5px 0 0; }
    .tab-switch { display: flex; margin-bottom: 20px; }
    .tab-switch button {
      flex: 1; padding: 10px; border: 1px solid #ddd; background: #f5f5f5;
      cursor: pointer; font-size: 14px; transition: all 0.3s;
    }
    .tab-switch button.active { background: #97144d; color: white; border-color: #97144d; }
    .tab-switch button:first-child { border-radius: 6px 0 0 6px; }
    .tab-switch button:last-child { border-radius: 0 6px 6px 0; }
    .form-group { margin-bottom: 16px; }
    .form-group label { display: block; margin-bottom: 6px; color: #333; font-weight: 500; }
    .form-group input {
      width: 100%; padding: 10px 12px; border: 1px solid #ddd; border-radius: 6px;
      font-size: 14px; box-sizing: border-box;
    }
    .form-group input:focus { outline: none; border-color: #97144d; }
    .btn-primary {
      width: 100%; padding: 12px; background: #97144d; color: white;
      border: none; border-radius: 6px; font-size: 16px; cursor: pointer;
      margin-top: 10px; transition: background 0.3s;
    }
    .btn-primary:hover { background: #7a1040; }
    .btn-primary:disabled { opacity: 0.6; cursor: not-allowed; }
    .error-message {
      margin-top: 15px; padding: 10px; background: #ffe6e6;
      color: #cc0000; border-radius: 6px; text-align: center;
    }
  `]
})
export class LoginComponent {
  activeTab = 'login';
  loading = false;
  error = '';

  loginData: LoginRequest = { username: '', password: '' };
  registerData: RegisterRequest = { username: '', password: '', email: '', fullName: '' };

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    this.loading = true;
    this.error = '';
    this.authService.login(this.loginData).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'Invalid credentials';
      }
    });
  }

  register(): void {
    this.loading = true;
    this.error = '';
    this.authService.register(this.registerData).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'Registration failed';
      }
    });
  }
}
