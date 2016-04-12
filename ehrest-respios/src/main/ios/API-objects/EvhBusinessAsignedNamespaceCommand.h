//
// EvhBusinessAsignedNamespaceCommand.h
// generated at 2016-04-12 19:00:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBusinessAsignedNamespaceCommand
//
@interface EvhBusinessAsignedNamespaceCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* targetId;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

