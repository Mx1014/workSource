//
// EvhAclinkCreateDoorAuthListCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCreateDoorAuthCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkCreateDoorAuthListCommand
//
@interface EvhAclinkCreateDoorAuthListCommand
    : NSObject<EvhJsonSerializable>


// item type EvhCreateDoorAuthCommand*
@property(nonatomic, strong) NSMutableArray* auths;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

