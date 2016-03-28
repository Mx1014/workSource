//
// EvhSyncUserContactCommand.h
// generated at 2016-03-25 19:05:20 
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

