//
// EvhListRegisterUsersResponse.h
// generated at 2016-03-28 15:56:09 
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

