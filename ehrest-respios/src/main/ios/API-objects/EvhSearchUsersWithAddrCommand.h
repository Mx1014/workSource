//
// EvhSearchUsersWithAddrCommand.h
// generated at 2016-04-06 19:10:42 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchUsersWithAddrCommand
//
@interface EvhSearchUsersWithAddrCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSString* cellPhone;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

