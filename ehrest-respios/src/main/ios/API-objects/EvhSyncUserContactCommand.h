//
// EvhSyncUserContactCommand.h
// generated at 2016-04-07 15:16:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhContact.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSyncUserContactCommand
//
@interface EvhSyncUserContactCommand
    : NSObject<EvhJsonSerializable>


// item type EvhContact*
@property(nonatomic, strong) NSMutableArray* contacts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

