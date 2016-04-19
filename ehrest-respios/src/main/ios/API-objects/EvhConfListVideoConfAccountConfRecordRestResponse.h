//
// EvhConfListVideoConfAccountConfRecordRestResponse.h
// generated at 2016-04-19 12:41:55 
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
