//
// EvhConfListVideoConfAccountConfRecordRestResponse.h
// generated at 2016-03-31 10:18:21 
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
