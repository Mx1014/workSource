//
// EvhSearchInvitatedUserCommand.h
// generated at 2016-03-25 17:08:11 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchInvitatedUserCommand
//
@interface EvhSearchInvitatedUserCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* userPhone;

@property(nonatomic, copy) NSString* inviterPhone;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

