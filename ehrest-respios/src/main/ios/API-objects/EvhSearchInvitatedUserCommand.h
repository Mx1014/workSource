//
// EvhSearchInvitatedUserCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
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

