//
// EvhBusinessAsignedNamespaceCommand.h
// generated at 2016-04-19 12:41:53 
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

