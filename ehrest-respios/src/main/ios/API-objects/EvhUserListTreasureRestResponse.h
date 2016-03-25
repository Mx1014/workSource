//
// EvhUserListTreasureRestResponse.h
// generated at 2016-03-25 15:57:24 
//
#import "RestResponseBase.h"
#import "EvhListTreasureResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListTreasureRestResponse
//
@interface EvhUserListTreasureRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListTreasureResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
