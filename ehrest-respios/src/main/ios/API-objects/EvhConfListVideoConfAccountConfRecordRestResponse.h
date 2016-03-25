//
// EvhConfListVideoConfAccountConfRecordRestResponse.h
// generated at 2016-03-25 15:57:24 
//
#import "RestResponseBase.h"
#import "EvhListVideoConfAccountConfRecordResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfListVideoConfAccountConfRecordRestResponse
//
@interface EvhConfListVideoConfAccountConfRecordRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListVideoConfAccountConfRecordResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
