//
// EvhQuitAndTransferPrivilegeCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQuitAndTransferPrivilegeCommand
//
@interface EvhQuitAndTransferPrivilegeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* groupId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

