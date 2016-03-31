//
// EvhSyncUserContactCommand.h
// generated at 2016-03-28 15:56:08 
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

