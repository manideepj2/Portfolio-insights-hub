import { Component, inject, signal, ViewChild, ElementRef } from '@angular/core';

import { CommonModule } from '@angular/common';

import { HttpClient } from '@angular/common/http';

import { PortfolioApiService } from '../../core/services/portfolio-api';

@Component({
  selector: 'app-upload',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './upload.html',
  styleUrl: './upload.css',
})
export class UploadComponent {
  private apiService = inject(PortfolioApiService);

  private http = inject(HttpClient);

  uploadMessage = signal<string | null>(null);

  isUploading = signal(false);

  uploadError = signal<string | null>(null);

  selectedFile = signal<File | null>(null);

  isDragOver = signal(false);

  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (input.files?.length) {
      this.selectedFile.set(input.files[0]);
    }
  }

  triggerFileInput(): void {
    this.fileInput.nativeElement.click();
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    this.isDragOver.set(true);
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    this.isDragOver.set(false);
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    this.isDragOver.set(false);

    const files = event.dataTransfer?.files;
    if (files?.length) {
      this.selectedFile.set(files[0]);
    }
  }

  clearFile(): void {
    this.selectedFile.set(null);
    if (this.fileInput) {
      this.fileInput.nativeElement.value = '';
    }
  }

  uploadFile(): void {
    const file = this.selectedFile();

    if (!file) {
      this.uploadError.set('Please select a CSV file.');

      return;
    }

    this.isUploading.set(true);

    this.uploadError.set(null);

    this.uploadMessage.set(null);

    this.apiService.getUploadUrl().subscribe({
      next: (response) => {
        this.http.put(response.uploadUrl, file).subscribe({
          next: () => {
            this.uploadMessage.set(`${file.name} uploaded successfully`);

            this.isUploading.set(false);
            setTimeout(() => {
              window.location.reload();
            }, 1500);
          },

          error: () => {
            this.uploadError.set('Failed to upload file to S3.');

            this.isUploading.set(false);
          },
        });
      },

      error: () => {
        this.uploadError.set('Failed to generate upload URL.');

        this.isUploading.set(false);
      },
    });
  }
}
