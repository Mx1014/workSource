//
// EvhFavoriteBusinessesCommand.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhFavoriteBusinessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFavoriteBusinessesCommand
//
@interface EvhFavoriteBusinessesCommand
    : NSObject<EvhJsonSerializable>


// item type EvhFavoriteBusinessDTO*
@property(nonatomic, strong) NSMutableArray* bizs;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

