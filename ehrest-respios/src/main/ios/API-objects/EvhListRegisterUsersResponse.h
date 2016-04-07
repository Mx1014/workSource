//
// EvhListRegisterUsersResponse.h
// generated at 2016-04-07 17:57:41 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhUserInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRegisterUsersResponse
//
@interface EvhListRegisterUsersResponse
    : NSObject<EvhJsonSerializable>


// item type EvhUserInfo*
@property(nonatomic, strong) NSMutableArray* values;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

