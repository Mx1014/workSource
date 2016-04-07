//
// EvhConfListVideoConfAccountConfRecordRestResponse.h
// generated at 2016-04-07 15:16:53 
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
