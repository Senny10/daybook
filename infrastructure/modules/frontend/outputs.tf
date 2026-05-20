output "cloudfront_domain_name" {
  description = "CloudFront distribution domain name — the public URL"
  value       = aws_cloudfront_distribution.frontend.domain_name
}

output "cloudfront_distribution_id" {
  description = "CloudFront distribution ID — needed for cache invalidation"
  value       = aws_cloudfront_distribution.frontend.id
}

output "s3_bucket_name" {
  description = "S3 bucket name — needed for file upload"
  value       = aws_s3_bucket.frontend.id
}